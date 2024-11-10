FROM alpine:3.20
#Install java
RUN apk update
RUN apk add openjdk17
#Build project
COPY . /product-calculator
WORKDIR /product-calculator
RUN ./gradlew clean build
WORKDIR /
RUN cp /product-calculator/build/libs/product-calculator.jar .
RUN rm -rf /product-calculator
#Run image
ENTRYPOINT ["java", "--enable-preview", "-Dspring.profiles.active=stage", "-Djava.security.egd=file:/dev/./urandom","-jar","/product-calculator.jar"]