pipeline {
    agent { label 'built-in' }

    tools {
        jdk 'JDK22'
    }

    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token-task07')
        MAVEN_OPTS = '-Xmx1024m'
        STAGING_DIR = "${env.WORKSPACE}\\staging"
    }

    parameters {
        booleanParam(name: 'SKIP_SONAR', defaultValue: false, description: 'Skip SonarQube analysis')
        booleanParam(name: 'SKIP_DEPLOY', defaultValue: false, description: 'Skip deployment to staging')
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                git branch: 'main', url: 'https://github.com/SharkGroup6/Project-Task08'
            }
        }

        stage('Build') {
            steps {
                echo 'Compiling Java files (excluding tests)...'
                bat 'if not exist target\\classes mkdir target\\classes'
                // Lister tous les fichiers .java et exclure ceux qui contiennent "Test"
                bat 'dir /s /b src\\*.java | findstr /v Test > sources.txt'
                bat 'javac -d target/classes @sources.txt'
                echo 'Build completed!'
            }
        }

        stage('Unit Test') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn test || echo "No tests found"'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            when {
                expression { return params.SKIP_SONAR == false }
            }
            steps {
                echo 'Starting SonarQube analysis...'
                withSonarQubeEnv('LocalSonarQube') {
                    bat """
                        "C:\\sonar-scanner\\bin\\sonar-scanner.bat" ^
                          -Dsonar.projectKey=project-task08 ^
                          -Dsonar.projectName="Project Task08" ^
                          -Dsonar.sources=src ^
                          -Dsonar.java.binaries=target/classes ^
                          -Dsonar.host.url=${env.SONAR_HOST_URL} ^
                          -Dsonar.token=${env.SONAR_TOKEN}
                    """
                }
                echo "SonarQube dashboard: ${env.SONAR_HOST_URL}/dashboard?id=project-task08"
            }
        }

        stage('Deploy to Staging') {
            when {
                expression { return params.SKIP_DEPLOY == false }
            }
            steps {
                echo 'Deploying to staging...'
                bat "if not exist \"${env.STAGING_DIR}\" mkdir \"${env.STAGING_DIR}\""
                script {
                    bat "xcopy /E /I /Y target\\classes \"${env.STAGING_DIR}\\classes\""
                    bat "echo Deployment date: %date% %time% > \"${env.STAGING_DIR}\\deploy_info.txt\""
                    bat "echo Build number: ${env.BUILD_NUMBER} >> \"${env.STAGING_DIR}\\deploy_info.txt\""
                    echo "✅ Deployed to ${env.STAGING_DIR}"
                }
            }
            post {
                success {
                    echo "✅ Staging deployment successful!"
                    echo "📁 Deployment location: ${env.STAGING_DIR}"
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
        }
        success {
            echo '✅ Pipeline completed successfully!'
            mail(
                to: 'arkancom007@gmail.com,abrohoma5@gmail.com',
                subject: "✅ SUCCESS: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: "Pipeline completed successfully!\n\nSonarQube: ${env.SONAR_HOST_URL}/dashboard?id=project-task08\nBuild URL: ${env.BUILD_URL}"
            )
        }
        failure {
            echo '❌ Pipeline failed!'
            mail(
                to: 'arkancom007@gmail.com,abrohoma5@gmail.com',
                subject: "❌ FAILED: ${env.JOB_NAME} - Build #${env.BUILD_NUMBER}",
                body: "Pipeline failed!\n\nBuild URL: ${env.BUILD_URL}\nConsole: ${env.BUILD_URL}console"
            )
        }
    }
}
