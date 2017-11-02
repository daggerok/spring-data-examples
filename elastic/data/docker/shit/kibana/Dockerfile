FROM docker.elastic.co/kibana/kibana:5.6.3
MAINTAINER Maksim Kostromin https://github.com/daggerok
#HEALTHCHECK --interval=15s --timeout=15s --retries=15 CMD curl -f http://127.0.0.1:5601/ || exit 1
HEALTHCHECK --timeout=15s --retries=15 CMD curl -f http://127.0.0.1:5601/ || exit 1
