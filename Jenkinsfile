pipeline {
    environment {
        repository = "mirea720/fitmate"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-token')
    }

    triggers {
        pollSCM('* * * * *')
    }

    agent any

    stages('Git Clone') {
        steps {
            git 'https://github.com/mirea70/fitmate-back'
        }
    }

    stages('Build Jar') {
        steps {
            sh "chmod +x gradlew"
            echo 'mate-service Build...'
            sh "./gradlew :app-mate:clean :app-mate:build -Pprofile=prd -x test"
        }
    }

    stages('Build Image') {
        steps {
            sh "cd modules/app/app-mate"
            echo 'mate-service docker build...'
            sh "docker build -t mate-service -f ./Dockerfile ."
        }
    }

    stages('Image Push') {
        steps {
            sh "echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin"
            sh "docker push $repository:latest"
        }
    }

    stages('Image Clean') {
        steps {
            sh "docker rmi $repository:latest"
        }
    }

    stages('Deploy') {
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