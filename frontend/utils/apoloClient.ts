import {createHttpLink} from "apollo-link-http"
import fetch from 'node-fetch'
import ApolloClient from "apollo-client"
import {InMemoryCache} from "apollo-cache-inmemory"

const clientUri = process.env.CLIENT_URI || "http://localhost:4000"
const link = createHttpLink({
    uri: clientUri,
    fetch: fetch
})

const client = new ApolloClient({
    link: link,
    cache: new InMemoryCache(),
})

export default client
