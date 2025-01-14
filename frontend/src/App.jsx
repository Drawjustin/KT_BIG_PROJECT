import './App.css'
import Navbar from './_components/navbar/Navbar'
import Footer from './_components/footer/Footer'
import Input from './_components/button/Input'
import Button from './_components/button/Button'

function App() {

  return (
    <>
      <Navbar/>
      <Input>이메일을 입력해주세요</Input>
      <Button>로그인</Button>
      <Footer/>
    </>
  )
}

export default App
