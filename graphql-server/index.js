const {ApolloServer, gql} = require('apollo-server')

const {RESTDataSource} = require('apollo-datasource-rest')

const express = require('express')
const protoLoader = require('@grpc/proto-loader')
const grpc = require('grpc')
const bodyParser = require('body-parser')

const greetingProtoPath = '../api/src/main/proto/greeting.proto'
const greetingProtoDefinition = protoLoader.loadSync(greetingProtoPath)
const greetingPackageDefinition = grpc.loadPackageDefinition(greetingProtoDefinition).example.grpc.helloworld

const client = new greetingPackageDefinition.GreetingService(
  "localhost:8080", grpc.credentials.createInsecure()
)

const GetGreetings = (req, res) => {
  client.GetGreetings({}, (err, result) => {
    res.json(result)
  })
}

const grpcRoutes = {
  GetGreetings
}

const router = express.Router()

router.get('/greetings', grpcRoutes.GetGreetings)

const app = express()
app.use(bodyParser.urlencoded({ extended: false}))
app.use(bodyParser.json())

app.use('/api', router)

app.listen(3000, () => {
  console.log('Server listening on port 3000')
})

// class HelloWorldAPI extends RESTDataSource {
//   constructor() {
//     super()
//     this.baseURL = 'http://localhost:8080/'
//   }
//
//   async getHelloWorld(id) {
//     return this.get(`hello-worlds/${id}`)
//       .then(response => {
//         return response.message
//       })
//       .catch(error => console.error(error))
//   }
//
//   async getHelloWorlds() {
//     return await this.get('hello-worlds')
//       .then(messages => messages.map(it => it.message))
//       .catch(error => console.error(error))
//   }
//
//   async saveHelloWorld(message) {
//     return await this.post(
//       `hello-worlds?message=${message}`
//     )
//       .then(isSaved => isSaved)
//       .catch(error => console.error(error))
//   }
// }
//
// // TODO: define hello world
// const typeDefs = gql`
//     type Query {
//         message(id: Int!): String
//         messages: [String!]!
//     }
//     type Mutation {
//         message(message: String!): Boolean
//     }
// `
//
// const resolvers = {
//   Query: {
//     message: async (_source, {id}, {dataSources}) => {
//       const message = await dataSources.helloWorldAPI.getHelloWorld(id)
//       return message
//     },
//     messages: async (_source, {}, {dataSources}) => {
//       const messages = await dataSources.helloWorldAPI.getHelloWorlds()
//       return messages
//     }
//   },
//   Mutation: {
//     message: async (_source, {message}, {dataSources}) => {
//       await dataSources.helloWorldAPI.saveHelloWorld(message)
//     }
//   }
// }
//
// const server = new ApolloServer({
//   typeDefs,
//   resolvers,
//   dataSources: () => {
//     return {
//       helloWorldAPI: new HelloWorldAPI()
//     }
//   }
// })
//
// server.listen().then(({url}) => {
//   console.log(`Server ready at ${url}`)
// })