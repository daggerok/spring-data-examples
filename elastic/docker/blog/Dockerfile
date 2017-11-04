FROM nginx:1.12.2-alpine
MAINTAINER Maksim Kostromin https://github.com/daggerok
RUN apk --no-cache add curl
HEALTHCHECK --timeout=10s --retries=5 CMD curl -f http://127.0.0.1/ || exit 1
COPY ./blog/index.html /usr/share/nginx/html/index.html
COPY ./blog/dist /usr/share/nginx/html/dist
