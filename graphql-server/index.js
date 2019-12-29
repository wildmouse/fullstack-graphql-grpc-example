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
}

const typeDefs = gql`
    type Query {
        message(id: Int!): String
        messages: [String!]!
    }
`

const resolvers = {
  Query: {
    message: async (_source, {id}, {dataSources}) => {
      const message = await dataSources.helloWorldAPI.getHelloWorld(id)
      console.log(message)
      return message
    },
    messages: async (_source, {}, {dataSources}) => {
      const messages = await dataSources.helloWorldAPI.getHelloWorlds()
      console.log(messages)
      return messages
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