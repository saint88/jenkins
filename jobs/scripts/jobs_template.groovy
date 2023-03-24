timeout(60) {
   node('maven-slave') {
      stage("Checkout") {
         //checkout scm
        git branches: master, credentialsId: jenkins, url: git@github.com:saint88/jenkins.git
      }
      stage("Deploy changes to jenkins") {
         sh "jenkins-jobs --conf ${WORKSPACE}/jobs/conf/jenkins-job-builder.ini update ./jobs"
      }
  }
}
