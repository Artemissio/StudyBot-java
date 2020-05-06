def IMAGE_NAME="StudyBot"
def CONTAINER_NAMES=["bot-container-1", "bot-container-2", "bot-admin-container-1"]
def CONTAINER_ARGS_MAP=["bot-container-1":["client", "1117493466:AAF1l0-iif8pauA2czuiW6JK7St4Bipp9n4", "StudyBotNo1"],
                        "bot-container-2":["client", "1025575343:AAGWW02oyIXwZMXnTMKSXqz4J1z0V0XdhBc", "StudyBotNo2"],
                        "bot-admin-container-1": ["admin", "1087308657:AAEPGzxzTMZm84hbKHb5oaDpt2v1wut0pvQ", "StudyAdminBot"]]

def IMAGE_TAG="latest"
def DOCKER_HUB_USER="artemissio"

node {

    stage('Initialize'){
        def dockerHome = tool 'Jenkins-Docker'
        def mavenHome  = tool 'Jenkins-Maven'
        env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
    }

    stage('Checkout') {
        checkout scm
    }
    /*Перебудувати проект та сформувати jar-файл*/
    stage('Build'){
        sh "mvn clean package"
    }
    /*Видалити зображення, якщо воно не використовується ні одним контейнером*/
    stage("Image Prune"){
        imagePrune(IMAGE_NAME)
    }
    /*Сформувати нове зображення*/
    stage('Image Build'){
        imageBuild(IMAGE_NAME, IMAGE_TAG)
    }
    /*Внести нове зображення до Docker Registry*/
    stage('Push to Docker Registry'){
        withCredentials([usernamePassword(credentialsId: 'dockerHubAccount', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            pushToImage(IMAGE_NAME, IMAGE_TAG, USERNAME, PASSWORD)
        }
    }
    /*Отримати оновлене зображення*/
    stage('Pull updated Image'){
        imagePull(IMAGE_NAME, DOCKER_HUB_USER)
    }
    /*Зупинити та видалити контейнери, які використовували застаріле зображення, та запустити ботів на контейнерах з оновленим зображенням*/
    stage('Run App'){
        CONTAINER_NAMES.each{ item ->
            runApp(item, CONTAINER_ARGS_MAP.get(item).get(0), CONTAINER_ARGS_MAP.get(item).get(1), CONTAINER_ARGS_MAP.get(item).get(2), IMAGE_NAME, IMAGE_TAG, DOCKER_HUB_USER)
        }
    }

}

def imagePrune(imageName){
    try {
        sh "docker image prune -f"
    } catch(error){}
}

def imageBuild(imageName, tag){
    sh "docker build -t $imageName:$tag  -t $imageName --pull --no-cache ."
    echo "Image build complete"
}

def pushToImage(imageName, tag, dockerUser, dockerPassword){
    sh "docker login -u $dockerUser -p $dockerPassword"
    sh "docker tag $imageName:$tag $dockerUser/$imageName:$tag"
    sh "docker push $dockerUser/$imageName:$tag"
    echo "Image push complete"
}

def imagePull(imageName, dockerHubUser){
    sh "docker pull $dockerHubUser/$imageName"
    echo "Updated image pulled"
}

def runApp(containerName, arg0, arg1, arg2, imageName, tag, dockerHubUser){
    sh "docker stop $containerName || true && docker rm $containerName || true"
    sh "docker run -d --rm --network host --name $containerName $dockerHubUser/$imageName:$tag $arg0 $arg1 $arg2"
    echo "$containerName started"
}
