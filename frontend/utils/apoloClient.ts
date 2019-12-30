import {createHttpLink} from "apollo-link-http"
import ApolloClient from "apollo-client"
import {InMemoryCache} from "apollo-cache-inmemory"

const link = createHttpLink({
    uri: "http://localhost:4000",
    fetch: fetch
})

const client = new ApolloClient({
    link: link,
    cache: new InMemoryCache(),
})

export default client
