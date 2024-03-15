pipeline {
    environment {
        repository = "mirea720/fitmate"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-token')
    }

//     triggers {
//         pollSCM('* * * * *')
//     }

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
                    sh "./gradlew :app-mate:clean :app-mate:build -Pprofile=prd -x test"
                }
            }
        }

        stage('Build Image') {
            steps {
                script {
                    echo 'mate-service docker build...'
                    dir('modules/app/app-mate') {
                        sh "docker build -t mate-service -f ./Dockerfile ."
                    }
                }
            }
        }

        stage('Image Push') {
            steps {
                script {
                    sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
                    sh "docker push $repository:latest"
                }
            }
        }

        stage('Image Clean') {
            steps {
                sh "docker rmi $repository:latest"
            }
        }

        stage('Deploy') {
            steps {
                ssh_publisher(
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
                                                                '''docker rmi -f mirea720/fitmate \
                                               ~/backend/deploy.sh \
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