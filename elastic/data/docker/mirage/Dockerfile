FROM appbaseio/mirage:v0.7.0
MAINTAINER Maksim Kostromin https://github.com/daggerok
HEALTHCHECK --interval=15s --timeout=15s --retries=15 CMD wget -qs http://127.0.0.1:3030/ || exit 1
