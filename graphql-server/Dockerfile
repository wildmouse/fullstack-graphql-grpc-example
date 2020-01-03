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
CMD node index.js
