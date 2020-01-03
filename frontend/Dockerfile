FROM mhart/alpine-node:10 AS builder
WORKDIR /app
COPY package.json .
RUN yarn install
COPY . .
#RUN yarn build && yarn --production

FROM mhart/alpine-node:10
WORKDIR /app
COPY --from=builder /app .
EXPOSE 3000
CMD CLIENT_URI=http://greeting_bff:4000 yarn dev
