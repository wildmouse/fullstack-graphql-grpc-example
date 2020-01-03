import React, {useCallback, useState} from 'react'
import client from "../utils/apoloClient"
import gql from "graphql-tag"
import {ApolloQueryResult} from "apollo-client"
import {useRouter} from "next/router"

export type Greeting = {
    id?: number
    message: string
}

interface Props {
    greetings: Greeting[]
}

const Home = ({greetings}: Props) => {
    const router = useRouter()

    const [message, setMessage] = useState('')

  const onClickGreet = useCallback(async () => {
      if (message == '') {
          return
      }
    const isSaved = await client.mutate({
      mutation: gql`
          mutation {
              greeting(message: "${message}") {
                  id
                  message
              }
          }
      `
    })
    .then(result => {
        console.log(result)
        return true
    })
    .catch(error => {
        console.error(error)
        return false
    })

      if (isSaved) {
          await router.push('/')
      }
  }, [message])

    if (greetings.length == 0) {
        return (
            <>
                <input value={message} onChange={(e) => setMessage(e.target.value)}/>
                <button onClick={onClickGreet}>Greet</button>
                <p>There is no greeting. Please say hi!</p>
            </>
        )
    }
    return (
        <>
            <input value={message} onChange={(e) => setMessage(e.target.value)}/>
            <button onClick={onClickGreet}>Greet</button>
            <ul>
                {
                    greetings.map((greeting) =>
                        <li key={greeting.id}>
                            <a href={`/hello-world/${greeting.id}`}>See greeting {greeting.id}</a>
                        </li>
                    )
                }
            </ul>
        </>
    )

}

Home.getInitialProps = async () => {
  const greetings = await client.query({
    query: gql`
        query {
            greetings {
                id
                message
            }
        }
    `
  })
  .then((result: ApolloQueryResult<{ greetings: Greeting[] }>) => result.data.greetings)
  .catch(error => console.error(error))

    return {
        greetings: greetings
    }
}

export default Home
