pipeline {
  agent any
  environment { APP_ENV = 'dev' }

  stages {
    stage('Clone') {
      steps {
        // Authenticated checkout
        git branch: 'main',
            credentialsId: 'github-https-creds',
            url: 'https://github.com/MiruthyanJayanS/Bstack-demo-Automation-Testing-Capstone-Project.git'
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

        cucumber fileIncludePattern: 'target/cucumber-reports/*.json',
                 buildStatus: 'UNSTABLE',
                 classifications: [[key: 'Env', value: "${env.APP_ENV}"]]

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

    stage('Commit & Push Changes') {
      steps {
        script {
          echo 'Checking for changes to push...'
          withCredentials([usernamePassword(
            credentialsId: 'github-https-creds',
            usernameVariable: 'GIT_USERNAME',
            passwordVariable: 'GIT_PASSWORD'
          )]) {
            bat """
              @echo off
              git config user.email "jenkins@pipeline.com"
              git config user.name  "Jenkins CI"
              git status
              git add .
              REM Commit only if there are changes
              git diff --cached --quiet || git commit -m "Jenkins: Auto-commit after build"
              REM Ensure push is authenticated with HTTPS credential
              git remote set-url origin https://%GIT_USERNAME%:%GIT_PASSWORD%@github.com/MiruthyanJayanS/Bstack-demo-Automation-Testing-Capstone-Project.git
              REM Prefer Multibranch BRANCH_NAME; else fallback to GIT_BRANCH or main
              if not "%BRANCH_NAME%"=="" (
                git push origin %BRANCH_NAME%
              ) else (
                if not "%GIT_BRANCH%"=="" (
                  git push origin %GIT_BRANCH%
                ) else (
                  git push origin HEAD:main
                )
              )
            """
          }
        }
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
