node {
//   def build_tag
  def registry_addr = "harbor.pecado.com"
  def maintainer_name = "pecado"
  def dp_image, gateway_image, ims_image, system_image, uaa_image
  def version = "1.1"

  stage('Git Clone') {
    git branch: 'dev', credentialsId: 'github', url: 'git@github.com:batizhao/pecado.git'
  }

  stage('Build Maven Package') {
//     build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
    withMaven(maven: 'maven', mavenSettingsConfig: 'maven-settings') {
        sh "mvn clean package -DNACOS_HOST=nacos.pecado.com -DNACOS_PORT=80"
    }
  }

  stage('Build Docker Image') {

    dir('pecado-dp') {
      image_name = "${registry_addr}/${maintainer_name}/dp:${version}"
      dp_image = docker.build(image_name)
    }

    dir('pecado-gateway') {
      image_name = "${registry_addr}/${maintainer_name}/gateway:${version}"
      gateway_image = docker.build(image_name)
    }

    dir('pecado-ims/pecado-ims-web') {
      image_name = "${registry_addr}/${maintainer_name}/ims:${version}"
      ims_image = docker.build(image_name)
    }

    dir('pecado-system/pecado-system-web') {
      image_name = "${registry_addr}/${maintainer_name}/system:${version}"
      system_image = docker.build(image_name)
    }

    dir('pecado-uaa') {
      image_name = "${registry_addr}/${maintainer_name}/uaa:${version}"
      uaa_image = docker.build(image_name)
    }

  }

  stage('Push Docker Image') {
    docker.withRegistry('https://harbor.pecado.com', 'harbor-jiangsu-auth') {
      dp_image.push()
      gateway_image.push()
      ims_image.push()
      system_image.push()
      uaa_image.push()
    }
  }
}