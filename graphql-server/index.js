const {ApolloServer, gql} = require('apollo-server')

const {RESTDataSource} = require('apollo-datasource-rest')

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

// TODO: define hello world
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
    message: async (_source, {id}, {dataSources}) => {
      const message = await dataSources.helloWorldAPI.getHelloWorld(id)
      return message
    },
    messages: async (_source, {}, {dataSources}) => {
      const messages = await dataSources.helloWorldAPI.getHelloWorlds()
      return messages
    }
  },
  Mutation: {
    message: async (_source, {message}, {dataSources}) => {
      await dataSources.helloWorldAPI.saveHelloWorld(message)
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