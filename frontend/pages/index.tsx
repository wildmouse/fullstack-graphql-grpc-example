import React, {useCallback, useState} from 'react'
import client from "../utils/apoloClient"
import gql from "graphql-tag"
import {ApolloQueryResult} from "apollo-client"
import {useRouter} from "next/router"

interface Props {
    messages: string[]
}

const Home = ({messages}: Props) => {
    const router = useRouter()

    const [message, setMessage] = useState('')

  const onClickGreet = useCallback(async () => {
      if (message == '') {
          return
      }
    const isSaved = await client.mutate({
      mutation: gql`
          mutation { message(message: "${message}")}
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

    return (
        <>
            <input value={message} onChange={(e) => setMessage(e.target.value)}/>
            <button onClick={onClickGreet}>Greet</button>
            {
                messages.map((message, index) =>
                    <p key={index}>
                        <a href={`/hello-world/${index + 1}`}>See greeting of {index + 1}</a>
                    </p>
                )
            }
        </>
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
