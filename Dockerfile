FROM redis:7.2.4-alpine3.19
EXPOSE 6379
CMD ["redis-server"]