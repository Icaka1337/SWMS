# Image with Alpine Linux
FROM eclipse-temurin:21-jre-alpine

# Update Alpine Linux
RUN apk update
RUN apk add bash
RUN apk add curl

RUN apk --no-cache --no-check-certificate add msttcorefonts-installer fontconfig && \
    update-ms-fonts && \
    fc-cache -vf

# Add tzdata for setting the timezone to CET
RUN apk add --no-cache tzdata
RUN cp /usr/share/zoneinfo/CET /etc/localtime
RUN echo "CET" > /etc/timezone