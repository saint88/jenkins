timeout(60) {
    node('maven-slave') {
        stage('Checkout') {
            git branch: "$BRANCH", credentialsId: 'jenkins', url: ''
        }
    }
}





timeout(60) {
    node('maven-slave') {
        timestamps {
            wrap([$class: 'BuildUser']){
                summary = """<b>Owner:</b> ${env.BUILD_USER}"""
                currentBuild.description = summary
            }
            stage('Checkout') {
                checkout scm
            }
            stage('Run all tests') {
                def jobs = [:]

                jobs['chrome'] = {
                    node('maven-slave') {
                    stage('Ui tests on chrome') {
                        if('chrome' in BROWSER) {
                            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                            build(job: 'ui-tests',
                            parameters: [
                                string(name: 'BRANCH', value: BRANCH),
                                string(name: 'BASE_URL', value: BASE_URL),
                                string(name: 'BROWSER', value: 'chrome'),
                                string(name: 'BROWSER_VERSION', value: BROWSER_VERSION)
                            ])
                            }
                        } else {
                            echo 'Skipping stage...'
                            Utils.markStageSkippedForConditional('keystone api tests')
                        }
                    }
                }

                parallel jobs
            }
            }
        }
    }
}