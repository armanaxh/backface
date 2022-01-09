def registryURL = 'registry:5000'
def project = 'samplewebapp'

pipeline {
  environment {
    imagename = "${registryURL}/samplewebapp"
    registryCredential = 'jenkins-user'
  }

  agent {
    kubernetes {
      yaml '''
kind: Pod
spec:
  containers:
  - name: dind
    image: registry:5000/builder:latest
    imagePullPolicy: IfNotPresent
    command:
      - dockerd-entrypoint.sh
      - "--insecure-registry=registry:5000"
      # - "--registry-mirror=$registryMirror" # TODO for better performance and insite image catch

    securityContext:
      privileged: true
    env:
      - name: http_proxy
        value: http://proxy.cafebazaar.org:3128
      - name: https_proxy
        value: http://proxy.cafebazaar.org:3128
      - name: no_proxy
        value: "*.test.example.com,.example2.com,localhost,127.0.0.1,10.96.0.0/12,192.168.59.0/24,192.168.39.0/24,registry,local,registry.local"
    resources:
      requests:
        ephemeral-storage: "4Gi"
  - name: deployer
    image: registry:5000/deployer:latest
    imagePullPolicy: IfNotPresent
    tty: true
    command:
      - /bin/cat
    env:
      - name: http_proxy
        value: http://proxy.cafebazaar.org:3128
      - name: https_proxy
        value: http://proxy.cafebazaar.org:3128
      - name: no_proxy
        value: "*.test.example.com,.example2.com,localhost,127.0.0.1,10.96.0.0/12,192.168.59.0/24,192.168.39.0/24,registry,local,registry.local"
    resources:
      requests:
        ephemeral-storage: "4Gi"
  serviceAccountName: deployer
'''
    }
  }

  stages {
    // stage('Build Docker image') {
    //   steps {
    //       git(
    //           url: 'https://github.com/armanaxh/duckface.git',
    //           credentialsId: 'jenkins-user',
    //           branch: 'main'
    //       )

    //       container('dind') {
    //             sh """
    //               cd $project
    //               docker build -f Dockerfile -t ${imagename}:${BUILD_NUMBER} .
    //               docker push ${imagename}:${BUILD_NUMBER}
    //             """
    //       }
    //         // ${BUILD_NUMBER} is better for image but ...
    //   }
    // }

    stage('Deploy') {
      steps {
          git(
              url: 'https://github.com/armanaxh/duckface.git',
              credentialsId: 'jenkins-user',
              branch: 'main'
          )

          container('deployer') {
                sh """
                  cd $project
                  echo Deploying samplewebapp on kubernetes
                  awk 'FNR==1{print "---"}1' kubernetes/*.yaml | sed s/IMAGE_NAME/"${imagename}:${BUILD_NUMBER}"/g - | kubectl apply -n default -f -
                """
          }
            // ${BUILD_NUMBER} is better for image but ...
      }
    }
  }
}

