import React from 'react'
import fetch from 'node-fetch'
import {createHttpLink} from 'apollo-link-http'
import ApolloClient, {ApolloQueryResult} from 'apollo-client'
import gql from 'graphql-tag'
import {InMemoryCache} from "apollo-cache-inmemory"

const link = createHttpLink({
    uri: "http://localhost:4000",
    fetch: fetch
})
const client = new ApolloClient({
    link: link,
    cache: new InMemoryCache(),
})

interface Props {
    message: string
}

const HelloWorld = ({message}: Props) => {
    return (
        <p>{message}</p>
    )
}

HelloWorld.getInitialProps = async (type) => {
  const message = await client.query({
    query: gql`
        query { message(id: ${type.query.id}) }
    `
  })
  .then((result: ApolloQueryResult<{ message: string }>) => result.data.message)
  .catch(error => console.error(error))

    return {
        message: message
    }
}

export default HelloWorld