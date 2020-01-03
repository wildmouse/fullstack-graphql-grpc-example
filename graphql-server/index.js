const {ApolloServer, gql} = require('apollo-server')

const {RESTDataSource} = require('apollo-datasource-rest')

const protoLoader = require('@grpc/proto-loader')
const grpc = require('grpc')

const greetingProtoPath = '../api/src/main/proto/greeting.proto'
const greetingProtoDefinition = protoLoader.loadSync(greetingProtoPath)
const greetingPackageDefinition = grpc.loadPackageDefinition(greetingProtoDefinition).example.grpc.helloworld

const clientUri = process.env.CLIENT_URI || "localhost:8080"
const client = new greetingPackageDefinition.GreetingService(
  clientUri, grpc.credentials.createInsecure()
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
class GreetingAPI extends RESTDataSource {
  constructor() {
    super()
    this.baseURL = 'http://localhost:8080/'
  }

  async getGreeting(id) {
    return this.get(`greetings/${id}`)
      .then(greeting => greeting)
      .catch(error => console.error(error))
  }

  async getHelloWorlds() {
    return await this.get('greetings')
      .then(greetings => greetings)
      .catch(error => console.error(error))
  }

  async saveHelloWorld(message) {
    return await this.post(`greetings?message=${message}`)
      .then(greeting => greeting)
      .catch(error => console.error(error))
  }
}

// TODO: define greeting type
const typeDefs = gql`
    type Greeting {
        id: Int!
        message: String!
    }

    type Query {
        greeting(id: Int!): Greeting
        greetings: [Greeting!]!
    }

    type Mutation {
        greeting(message: String!): Greeting!
    }
`

const resolvers = {
  Query: {
    greeting: async (_source, {id}) => {
      const {greeting} = await GetGreeting({id}, (err, result) => {
        return result
      })
      return greeting
    },
    greetings: async (_source, {}) => {
      const { greetings} = await GetGreetings({}, (err, result) => {
        return result
      })
      return greetings
    }
  },
  Mutation: {
    greeting: async (_source, {message}) => {
      const {greeting} = await SaveGreeting({message}, (err, result) => {
        console.log('result: ', result)
        return result
      })
      return greeting
    }
  }
}

const server = new ApolloServer({
  typeDefs,
  resolvers
})

server.listen().then(({url}) => {
  console.log(`Server ready at ${url}`)
})