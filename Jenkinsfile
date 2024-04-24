pipeline {
    environment {
        repository = "mirea720/fitmate"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-token')
    }
    agent any

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'main', credentialsId: 'github-token', url: 'https://github.com/mirea70/fitmate-back'
            }
        }

        stage('Build Jar') {
            steps {
                script {
                    sh "chmod +x gradlew"
                    echo 'mate-service Build...'
                    sh "./gradlew :adapter:clean :adapter:build -Pprofile=prd -x test"
                }
            }
        }

        stage('Build Image') {
            steps {
                script {
                    echo 'docker build...'
                    sh "docker build -t $repository:mate -f ./Dockerfile ."
                }
            }
        }

        stage('Image Push') {
            steps {
                script {
                    sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
                    sh "docker push $repository:mate"
                }
            }
        }

        stage('Images Clean') {
            steps {
                sh "docker rmi $repository:mate"
            }
        }

        stage('Deploy') {
            steps {
                sshPublisher(
                        continueOnError: false,
                        failOnError: true,
                        publishers:[
                                sshPublisherDesc(
                                        configName: 'instance-fitmate',
                                        verbose: true,
                                        transfers: [
                                                sshTransfer(
                                                        cleanRemote: false,
                                                        excludes: '',
                                                        execCommand:
                                                                '''docker rmi -f mirea720/fitmate:mate
                                                                    ~/backend/deploy.sh
                                                                    docker rmi -f $(docker images -f "dangling=true" -q)
                                                                ''',
                                                        execTimeout: 120000,
                                                        flatten: false,
                                                        makeEmptyDirs: false,
                                                        noDefaultExcludes: false,
                                                        patternSeparator: '[, ]+',
                                                        remoteDirectory: './backend/',
                                                        remoteDirectorySDF: false,
                                                )
                                        ]
                                )
                        ]
                )
            }
        }
    }
}