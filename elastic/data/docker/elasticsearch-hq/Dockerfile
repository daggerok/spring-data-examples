FROM nginx:1.12.2-alpine
MAINTAINER Maksim Kostromin https://github.com/daggerok
ARG ELASTICSEARCH_HQ_VERSION="2.0.3"
ENV ELASTICSEARCH_HQ_VERSION=${ELASTICSEARCH_HQ_VERSION}
RUN apk --no-cache add wget ca-certificates unzip curl \
 && wget https://github.com/royrusso/elasticsearch-HQ/archive/v${ELASTICSEARCH_HQ_VERSION}.zip -O /tmp/elasticsearch-HQ-${ELASTICSEARCH_HQ_VERSION}.zip \
 && unzip -q /tmp/elasticsearch-HQ-${ELASTICSEARCH_HQ_VERSION}.zip -d /tmp \
 && mv -f /tmp/elasticsearch-HQ-${ELASTICSEARCH_HQ_VERSION}/* /usr/share/nginx/html \
 && rm -rf /tmp/elasticsearch-HQ-${ELASTICSEARCH_HQ_VERSION}*
HEALTHCHECK --interval=15s --timeout=15s --retries=15 CMD wget -qS http://127.0.0.1/ || exit 1
