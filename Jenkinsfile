node {
//   def build_tag
  def registry_addr = "harbor.pecado.com"
  def maintainer_name = "pecado"
//   def image
  def version = "1.1"

  stage('Git Clone') {
    git branch: 'dev', credentialsId: 'github', url: 'git@github.com:batizhao/pecado.git'
  }

  stage('Build Maven Package') {
//     build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
    withMaven(maven: 'maven', mavenSettingsConfig: 'maven-settings') {
        sh "mvn clean package -Dmaven.test.skip=true -DNACOS_HOST=nacos.pecado.com -DNACOS_PORT=80"
    }
  }

  stage('Build Docker Image') {

    dir('pecado-dp') {
      image_name = "${registry_addr}/${maintainer_name}/dp:${version}"
      image = docker.build(image_name)
      image.push()
    }

    dir('pecado-gateway') {
      image_name = "${registry_addr}/${maintainer_name}/gateway:${version}"
      image = docker.build(image_name)
      image.push()
    }

    dir('pecado-ims/pecado-ims-web') {
      image_name = "${registry_addr}/${maintainer_name}/ims:${version}"
      image = docker.build(image_name)
      image.push()
    }

    dir('pecado-system/pecado-system-web') {
      image_name = "${registry_addr}/${maintainer_name}/system:${version}"
      image = docker.build(image_name)
      image.push()
    }

    dir('pecado-uaa') {
      image_name = "${registry_addr}/${maintainer_name}/uaa:${version}"
      image = docker.build(image_name)
      image.push()
    }

  }

//   stage('Push Docker Image') {
//     docker.withRegistry('https://harbor.pecado.com', 'harbor-jiangsu-auth') {
//       image.push()
//     }
//   }
}