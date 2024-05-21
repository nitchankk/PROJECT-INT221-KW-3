// const baseUrl = 'http://ip23kw3.sit.kmutt.ac.th:8080/itb-kk/v2'

const baseUrl = import.meta.env.VITE_API_URL

const fetchData = async (url, id = null) => {
  try {
    const response = await fetch(
      id ? `${baseUrl}/${url}/${id}` : `${baseUrl}/${url}`
    )
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const responseData = await response.json()
    if (id) {
      responseData.id = id
    }
    return responseData
  } catch (error) {
    console.error('Error fetching data:', error)
    throw error
  }
}

const postData = async (url, data) => {
  try {
    const response = await fetch(`${baseUrl}/${url}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const responseData = await response.json()
    if (response.status === 201) {
      console.log('Data posted successfully! Status code:', response.status)
      return { success: true, data: responseData, statusCode: response.status }
    } else {
      // handle other successful responses if needed
      return { success: true, data: responseData, statusCode: response.status }
    }
  } catch (error) {
    console.error('Error posting data:', error)
    throw error
  }
}

const putData = async (url, data) => {
  try {
    const response = await fetch(`${baseUrl}/${url}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const responseData = await response.json()
    console.log('Data updated successfully! Status code:', response.status)
    return { success: true, data: responseData, statusCode: response.status }
  } catch (error) {
    console.error('Error updating data:', error)
    throw error
  }
}

const deleteData = async (url) => {
  try {
    const response = await fetch(`${baseUrl}/${url}`, {
      method: 'DELETE'
    })
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const responseData = await response.json()
    if (response.status === 200) {
      console.log('Data deleted successfully! Status code:', response.status)
      return { success: true, data: responseData, statusCode: response.status }
    } else {
      // handle other successful responses if needed
      return { success: true, data: responseData, statusCode: response.status }
    }
  } catch (error) {
    console.error('Error deleting data:', error)
    throw error
  }
}

const deleteAndTransferData = async (url, newId) => {
  try {
    const response = await fetch(`${baseUrl}/${url}/${newId}`, {
      method: 'DELETE'
    })
    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`)
    }
    const responseData = await response.json()
    if (response.status === 200) {
      console.log(
        'Data deleted and transferred successfully! Status code:',
        response.status
      )
      return { success: true, data: responseData, statusCode: response.status }
    } else {
      // handle other successful responses if needed
      return { success: true, data: responseData, statusCode: response.status }
    }
  } catch (error) {
    console.error('Error deleting and transferring data:', error)
    throw error
  }
}

export default {
  fetchData,
  postData,
  putData,
  deleteData,
  deleteAndTransferData
}
