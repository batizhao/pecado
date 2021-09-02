node {
  def build_tag
  def registry_addr = "harbor.pecado.com:8888"
  def maintainer_name = "pecado"
  def dp_image, gateway_image, ims_image, system_image, uaa_image, oa_image
  def version = "1.4"

  stage('Git Clone') {
    git branch: 'dev', credentialsId: 'github', url: 'git@github.com:batizhao/pecado.git'
  }

  stage('Code Test') {
    withMaven(maven: 'maven', jdk: 'jdk11', mavenSettingsConfig: 'maven-settings') {
      sh "mvn clean test -Ptest -DNACOS_HOST=nacos.pecado.com -DNACOS_PORT=80"
    }
  }

  stage('Build Maven Package') {
    build_tag = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
    withMaven(maven: 'maven', jdk: 'jdk11', mavenSettingsConfig: 'maven-settings') {
        sh "mvn clean package -Dmaven.test.skip=true"
    }
  }

  stage('Build Docker Image') {

    dir('pecado-dp/pecado-dp-platform') {
      image_name = "${registry_addr}/${maintainer_name}/dp:${version}-${build_tag}"
      dp_image = docker.build(image_name)
    }

    dir('pecado-gateway') {
      image_name = "${registry_addr}/${maintainer_name}/gateway:${version}-${build_tag}"
      gateway_image = docker.build(image_name)
    }

    dir('pecado-ims/pecado-ims-web') {
      image_name = "${registry_addr}/${maintainer_name}/ims:${version}-${build_tag}"
      ims_image = docker.build(image_name)
    }

    dir('pecado-system/pecado-system-web') {
      image_name = "${registry_addr}/${maintainer_name}/system:${version}-${build_tag}"
      system_image = docker.build(image_name)
    }

    dir('pecado-uaa') {
      image_name = "${registry_addr}/${maintainer_name}/uaa:${version}-${build_tag}"
      uaa_image = docker.build(image_name)
    }

    dir('pecado-oa') {
      image_name = "${registry_addr}/${maintainer_name}/oa:${version}-${build_tag}"
      oa_image = docker.build(image_name)
    }

  }

  stage('Push Docker Image') {
    docker.withRegistry('https://harbor.pecado.com:8888', 'harbor-auth') {
      dp_image.push()
      gateway_image.push()
      ims_image.push()
      system_image.push()
      uaa_image.push()
      oa_image.push()
    }
  }
}