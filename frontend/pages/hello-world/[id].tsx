import React from 'react'
import {ApolloQueryResult} from 'apollo-client'
import gql from 'graphql-tag'
import client from "../../utils/apoloClient"
import {Greeting} from "../index";

interface Props {
    greeting: Greeting
}

const HelloWorld = ({greeting}: Props) => {
    return (
        <p>{greeting.message}</p>
    )
}

HelloWorld.getInitialProps = async ({query: {id}}) => {
  const greeting = await client.query({
    query: gql`
        query {
            greeting(id: ${id})  {
                id
                message
            }
        }
    `
  })
  .then((result: ApolloQueryResult<{ greeting: Greeting }>) => result.data.greeting)
  .catch(error => console.error(error))

    return {
        greeting: greeting
    }
}

export default HelloWorld