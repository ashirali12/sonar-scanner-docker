pipeline {
    agent any
    parameters{
      string defaultValue: 'master', description :'', name : 'branch_name', trim: false
      string defaultValue: 'git@github.com:VikashChoudahry/sonar-scanner-docker.git', description :'', name : 'ssh_url', trim: false
    }
    stages {
        stage('Pre Build') {
            steps {
                dir("/Users/vikash/.jenkins/workspace/sonar-scanner-docker") {
                    sh 'node --version'
                    sh "rm -rf *"
                    git branch: "${branch_name}", credentialsId: "gitaccess", url: "${ssh_url}"
                }
            }
        }
        stage('Build') {
            steps {
                dir("/Users/vikash/.jenkins/workspace/sonar-scanner-docker") {
                    script {
                        if ("${branch_name}" == "master") {
                          echo "I am working for branch: ${branch_name}"
                          echo "Run npm install ..."
                          sh "pwd"
                          sh "npm install"
                        }
                    }
                }
            }
        }
        stage('Coverage') {
            steps {
                dir("/Users/vikash/.jenkins/workspace/sonar-scanner-docker") {
                    script {
                        if ("${branch_name}" == "master") {
                          echo "I am working for branch: ${branch_name}"
                          echo "Generate the coverge in the lcov format ..."
                          sh "pwd"
                          sh "npm run coverage-lcov"
                          echo "Update lcov info. This is just a temp fix ..."
                          sh "find coverage/lcov.info -type f -exec sed -i.bak 's,/Users/vikash/.jenkins/workspace/sonar-scanner-docker,/usr/src,g' {} +;"
                        }
                    }
                }
            }
        }
        stage('Sonar Scanner') {
            steps {
                dir("/Users/vikash/.jenkins/workspace/sonar-scanner-docker") {
                    script {
                        if ("${branch_name}" == "master") {
                          echo "I am working for branch: ${branch_name}"
                          echo "Sonar scanning started ..."
                          sh "pwd"
                          sh "docker build --network=host --no-cache ."
                          sh "sonar-scanner"
                        }
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                dir("/Users/vikash/.jenkins/workspace/sonar-scanner-docker") {
                    script {
                        if("${branch_name}" == "master"){
                            echo "Deployment command need to be added here ..."
                            sh "pwd"
                            sh "rm -rf *"
                        }
                    }
                }
            }
        }
    }
}