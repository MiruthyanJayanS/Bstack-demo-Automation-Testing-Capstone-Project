pipeline {
  agent any
  options { skipDefaultCheckout(true) }  
  environment {
    APP_ENV    = 'dev'
    LOCAL_REPO = 'C:\\Users\\mirut\\Capstone-Project-workspace\\BstackDemoAutomation'
  }

  stages {
    stage('Trust local repo') {
      steps {
        ws(env.LOCAL_REPO) {
          bat 'git config --global --add safe.directory C:/Users/mirut/Capstone-Project-workspace/BstackDemoAutomation'
        }
      }
    }

    stage('Build & Test') {
      steps {
        ws(env.LOCAL_REPO) {
          echo 'Running TestNG + Cucumber via Maven...' 
          bat 'mvn -B clean test' 
        }
      }
    }

    stage('Publish Reports') {
      steps {
        ws(env.LOCAL_REPO) {
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
    }

    stage('Commit & Push (no reset)') {
      steps {
        ws(env.LOCAL_REPO) {
          withCredentials([gitUsernamePassword(credentialsId: 'Jenkins', gitToolName: 'Default')]) {
            bat '''
              @echo off
              git config user.email "mirthya.s.ad.2020@snsce.ac.in"
              git config user.name  "Admin"

              REM Ensure upstream is set once; ignore error if already set
              git branch --set-upstream-to=origin/main 1>NUL 2>NUL

              REM Commit local changes (does nothing if no changes)
              git add -A
              git commit -m "ci: auto-commit from Jenkins" || echo No local changes to commit

              REM Rebase onto origin without discarding local commits; auto-stash uncommitted edits
              git pull --rebase --autostash origin main

              REM Push current branch to origin/main
              if not "%BRANCH_NAME%"=="" (
                git push origin %BRANCH_NAME%
              ) else (
                git push origin HEAD:main
              )
            '''
          } 
        }
      }
    }

    stage('Deploy') {
      steps { echo "Deploying to ${env.APP_ENV}..." }
    }
  }

  post {
    always  { echo "Build result: ${currentBuild.currentResult}" } 
    success { echo '✅ Pipeline succeeded!' } 
    failure { echo '❌ Pipeline failed! Check Jenkins logs and reports.' } 
  }
}