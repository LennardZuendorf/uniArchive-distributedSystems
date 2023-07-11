FROM nginx:stable

COPY target/dist /usr/share/nginx/html

RUN apt-get update && apt-get upgrade -y \
    && apt-get install -y \
    curl \
    procps \
    psmisc  \
    && rm -rf /var/lib/apt/lists/*

