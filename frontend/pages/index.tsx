import React from 'react'
import client from "../utils/apoloClient"
import gql from "graphql-tag"
import {ApolloQueryResult} from "apollo-client"

interface Props {
    messages: string[]
}

const Home = ({messages}: Props) => {
    return messages.map((message, index) =>
        <p key={index}>
            <a href={`/hello-world/${index + 1}`}>See greeting of {index + 1}</a>
        </p>
    )
}

Home.getInitialProps = async () => {
  const messages = await client.query({
    query: gql`
        query { messages }
    `
  })
  .then((result: ApolloQueryResult<{ messages: string[] }>) => result.data.messages)
  .catch(error => console.error(error))

    return {
        messages: messages
    }
}

export default Home
