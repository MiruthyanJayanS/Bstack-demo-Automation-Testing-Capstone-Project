pipeline {
  agent any
  environment { APP_ENV = 'dev' }

  stages {
    stage('Clone') {
      steps {
        git branch: 'main', url: 'https://github.com/MiruthyanJayanS/Bstack-demo-Automation-Testing-Capstone-Project.git'
      }
    }

    stage('Build & Test') {
      steps {
        echo 'Running TestNG + Cucumber via Maven...'
        bat 'mvn -B clean test'
      }
    }

    stage('Publish Reports') {
      steps {
        echo 'Publishing Cucumber and Extent reports...'

        // Cucumber JSON -> rich Jenkins report
        cucumber fileIncludePattern: 'target/cucumber-reports/*.json',
                 buildStatus: 'UNSTABLE',
                 classifications: [[key: 'Env', value: "${env.APP_ENV}"]]

        // Extent HTML -> HTML Publisher (requires HTML Publisher plugin)
        publishHTML(target: [
          reportDir: 'test-output/ExtentReport',
          reportFiles: 'index.html',
          reportName: 'Extent Report',
          keepAll: true,
          allowMissing: false,
          alwaysLinkToLastBuild: true
        ])
      }
    }

    stage('Deploy') {
      steps {
        echo "Deploying to ${env.APP_ENV}..."
      }
    }
  }

  post {
    always { echo "Build result: ${currentBuild.currentResult}" }
    success { echo '✅ Pipeline succeeded!' }
    failure { echo '❌ Pipeline failed! Check Jenkins logs and reports.' }
  }
}
