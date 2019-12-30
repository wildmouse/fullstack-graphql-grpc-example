import React from 'react'
import {ApolloQueryResult} from 'apollo-client'
import gql from 'graphql-tag'
import client from "../../utils/apoloClient"

interface Props {
    message: string
}

const HelloWorld = ({message}: Props) => {
    return (
        <p>{message}</p>
    )
}

HelloWorld.getInitialProps = async ({query: {id}}) => {
  const message = await client.query({
    query: gql`
        query { message(id: ${id}) }
    `
  })
  .then((result: ApolloQueryResult<{ message: string }>) => result.data.message)
  .catch(error => console.error(error))

    return {
        message: message
    }
}

export default HelloWorld