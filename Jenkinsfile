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

        stage('Build Jar Mate Service') {
            steps {
                script {
                    sh "chmod +x gradlew"
                    echo 'mate-service Build...'
                    sh "./gradlew :app-mate:clean :app-mate:build -Pprofile=prd -x test"
                }
            }
        }

        stage('Build Jar Chat Service') {
                    steps {
                        script {
                            sh "chmod +x gradlew"
                            echo 'chat-service Build...'
                            sh "./gradlew :app-chat:clean :app-chat:build -Pprofile=prd -x test"
                        }
                    }
                }

        stage('Build Image Mate Service') {
            steps {
                script {
                    echo 'mate-service docker build...'
                    dir('modules/app/app-mate') {
                        sh "docker build -t $repository:mate -f ./Dockerfile ."
                    }
                }
            }
        }

        stage('Build Image Chat Service') {
                    steps {
                        script {
                            echo 'chat-service docker build...'
                            dir('modules/app/app-chat') {
                                sh "docker build -t $repository:chat -f ./Dockerfile ."
                            }
                        }
                    }
                }

        stage('Images Push') {
            steps {
                script {
                    sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
                    sh "docker push $repository:mate"
                    sh "docker push $repository:chat"
                }
            }
        }

        stage('Images Clean') {
            steps {
                sh "docker rmi $repository:mate"
                sh "docker rmi $repository:chat"
            }
        }

        stage('Deploy Mate Service') {
            steps {
                ssh_publisher(
                        continueOnError: false,
                        failOnError: true,
                        publishers:[
                                sshPublisherDesc(
                                        configName: 'instance-fitmate',
                                        verbose: true,
                                        transfers: [],
                                        execCommand:
                                            '''docker rmi -f mirea720/fitmate:mate \
                                               ~/backend/mate/mate-deploy.sh \
                                               docker rmi -f $(docker images -f "dangling=true" -q)
                                            ''',
                                        execTimeout: 120000
                                )
                        ]
                )
            }
        }

        stage('Deploy Chat Service') {
            steps {
                ssh_publisher(
                        continueOnError: false,
                        failOnError: true,
                        publishers:[
                                sshPublisherDesc(
                                        configName: 'instance-fitmate',
                                        verbose: true,
                                        transfers: [],
                                        execCommand:
                                            '''docker rmi -f mirea720/fitmate:chat \
                                               ~/backend/chat/chat-deploy.sh \
                                               docker rmi -f $(docker images -f "dangling=true" -q)
                                            ''',
                                        execTimeout: 120000
                                )
                        ]
                )
            }
        }
    }
}