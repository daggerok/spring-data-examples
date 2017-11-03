FROM nginx:1.12.2-alpine
MAINTAINER Maksim Kostromin https://github.com/daggerok
RUN apk --no-cache add curl
HEALTHCHECK --timeout=5s --retries=15 CMD curl -f http://127.0.0.1/ || exit 1
COPY ./data/blog/index.html /usr/share/nginx/html/index.html
COPY ./data/blog/dist /usr/share/nginx/html/dist
