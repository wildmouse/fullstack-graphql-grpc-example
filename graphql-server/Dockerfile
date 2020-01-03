FROM mhart/alpine-node:10 AS builder
WORKDIR /app
COPY ./graphql-server/package.json .
COPY ./api /api
RUN yarn install
COPY ./graphql-server .

FROM mhart/alpine-node:10
WORKDIR /app
COPY --from=builder /app .
COPY --from=builder /api /api
EXPOSE 4000
CMD CLIENT_URI=greeting_api:8080 node index.js
