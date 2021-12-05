# Labo SMTP - Mélissa Gehring et Maëlle Vogel

## Introduction, ce qu'il faut savoir

Notre projet permet d'envoyer des mails forgés à une liste de victimes. L'expéditeur et les récepteurs sont
choisi de façon aléatoire parmi une liste pré-défini par nos soins. Un mail est dit frogé quand il usurpre l'identité
d'un tiers et contient de fausses informations souvent dans un but malveillant.

## MockMock avec docker

Pour que MockMock puisse fonctionner dans un container il faut créer un Dockerfile à la racine du dossier.
Dans ce Dockerfile nous devons donner les informations nécessaire pour lancer l'exécutif jar.

    FROM openjdk:11
    COPY target/MockMock-1.4.0.one-jar.jar /mock.jar
    WORKDIR /
    CMD ["java", "-jar", "/mock.jar"]


Ensuite il faut construire l'image avec ``docker build -t mockmock .`` pour cela il faut se trouver à la racine du dossier
où se trouve le fichier Dockerfile.
Après cela on peut lancer MockMock mais il faut mapper les ports pour pouvoir utiliser l'interface web comme il se doit:

``docker run -d -p 8282:8282 -p 2525:25 mockmock``

Le port 25 ne sera pas utilisé car cela causait trop de problème (sur Debian 11).
Maintenant en allant sur [http://localhost:8282/](http://localhost:8282/) on a accès au serveur SMTP.
En lançant l'application créé pour le projet on voit que les mails arrivent bien sur MockMock

### Note

Sur Debian 11, docker prend presque 2 minutes à démarrer. Ce qui peut créer une erreur si on lance l'application prank trop tôt.

## Utilisation de notre application prank-SMTP

Les victimes sont listées dans le fichier *victims.utf8* dans le dossier *config* à la racine. Il faut mettre une adresse
mail par ligne.
Le contenu du mail se trouve dans le même dossier dans le fichier *content.utf8*. Chaque contenu doit contenir un
sujet et être séparé par 2 *.

Une fois ces deux fichiers remplis, il faut lancer main.java avec un argument qui est le nombre de groupe à créér.
Chaque groupe recevera un mail et un contenu aléatoire parmi ceux de la liste.

## Fonctionnement de notre application