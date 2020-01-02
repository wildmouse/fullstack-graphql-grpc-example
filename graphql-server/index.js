const {ApolloServer, gql} = require('apollo-server')

const {RESTDataSource} = require('apollo-datasource-rest')

const protoLoader = require('@grpc/proto-loader')
const grpc = require('grpc')

const greetingProtoPath = '../api/src/main/proto/greeting.proto'
const greetingProtoDefinition = protoLoader.loadSync(greetingProtoPath)
const greetingPackageDefinition = grpc.loadPackageDefinition(greetingProtoDefinition).example.grpc.helloworld

const client = new greetingPackageDefinition.GreetingService(
  "localhost:8080", grpc.credentials.createInsecure()
)

const GetGreeting = (params, context) => {
  return new Promise((resolve, reject) => {
    client.GetGreeting({id: params.id}, {}, (err, result) => {
      return resolve(result)
    })
  })
}

const GetGreetings = (params, context) => {
  return new Promise((resolve, reject) => {
    client.GetGreetings({}, {}, (err, result) => {
      return resolve(result)
    })
  })
}

const SaveGreeting = ({message}, context) => {
  return new Promise((resolve, reject) => {
    client.SaveGreeting({message}, {}, (err, result) => {
      return resolve(result)
    })
  })
}

// Leave as a sample
class HelloWorldAPI extends RESTDataSource {
  constructor() {
    super()
    this.baseURL = 'http://localhost:8080/'
  }

  async getHelloWorld(id) {
    return this.get(`hello-worlds/${id}`)
      .then(response => {
        return response.message
      })
      .catch(error => console.error(error))
  }

  async getHelloWorlds() {
    return await this.get('hello-worlds')
      .then(messages => messages.map(it => it.message))
      .catch(error => console.error(error))
  }

  async saveHelloWorld(message) {
    return await this.post(
      `hello-worlds?message=${message}`
    )
      .then(isSaved => isSaved)
      .catch(error => console.error(error))
  }
}

// TODO: define greeting type
const typeDefs = gql`
    type Query {
        message(id: Int!): String
        messages: [String!]!
    }
    type Mutation {
        message(message: String!): Boolean
    }
`

const resolvers = {
  Query: {
    message: async (_source, {id}) => {
      const {message} = await GetGreeting({id}, (err, result) => {
        return result
      })
      return message
    },
    messages: async (_source, {}) => {
      const {messages} = await GetGreetings({}, (err, result) => {
        return result
      })
      return messages
    }
  },
  Mutation: {
    message: async (_source, {message}) => {
      const { isSaved } = await SaveGreeting({message}, (err, result) => {
        return result
      })
      return isSaved
    }
  }
}

const server = new ApolloServer({
  typeDefs,
  resolvers,
  dataSources: () => {
    return {
      helloWorldAPI: new HelloWorldAPI()
    }
  }
})

server.listen().then(({url}) => {
  console.log(`Server ready at ${url}`)
})