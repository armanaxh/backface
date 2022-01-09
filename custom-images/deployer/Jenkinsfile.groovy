def registryURL = 'registry:5000'
def project = 'custom-images/deployer'

pipeline {
  environment {
    imagename = "${registryURL}/deployer:latest"
    registryCredential = 'jenkins-user'
  }

  agent {
    kubernetes {
      yaml '''
kind: Pod
spec:
  containers:
  - name: dind
    image: docker:stable-dind
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
'''
    }
  }

  stages {
    stage('Build') {
      steps {
          git(
              url: 'https://github.com/armanaxh/duckface.git',
              credentialsId: 'jenkins-user',
              branch: 'main'
          )

        container('dind') {
              sh """
                cd $project
                docker build -f Dockerfile -t ${imagename} .
                docker push ${imagename}
              """
        }
            // ${BUILD_NUMBER} is better for image but ...
      }
    }
  }
}

