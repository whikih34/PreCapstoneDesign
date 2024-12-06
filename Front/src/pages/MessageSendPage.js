import React, { useState } from 'react';
import styled from 'styled-components';
import { sendSMS, sendMMS } from '../services/messageService'; // 서비스 임포트
import { uploadImageToS3 } from '../services/imageUploadService';

const PageContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
`;

const FormContainer = styled.div`
  display: flex;
  justify-content: space-between;
  flex-grow: 1;
`;

const LeftPane = styled.div`
  flex: 1;
  padding-right: 20px;
`;

const RightPane = styled.div`
  flex: 1;
  padding-left: 20px;
`;

const SectionTitle = styled.h2`
  font-size: 24px;
  margin-bottom: 20px;
`;

const Textarea = styled.textarea`
  width: 100%;
  height: 100px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  margin-bottom: 10px;
  resize: none;
  background-color: #f5f5f5;
  pointer-events: none; /* 수정 불가능 */
  color: #333;
`;

const MessageTypeInfo = styled.p`
  font-size: 14px;
  color: red;
  margin-top: 10px;
  margin-bottom: 50px;
`;

const ImageTypeInfo = styled.p`
  font-size: 12px;
  color: black;
`;

const NumberInputSection = styled.div`
  margin-bottom: 20px;
`;

const SubSectionTitle = styled.h3`
  margin-bottom: 10px;
`;

const NumberInputGroup = styled.div`
  display: flex;
  gap: 10px;
  margin-bottom: 10px;

  input {
    flex: 1;
    padding: 10px;
    font-size: 16px;
    border: 1px solid #ccc;
  }
`;

const SubmitButton = styled.button`
  padding: 10px;
  background-color: blue;
  color: white;
  border: none;
  cursor: pointer;
  width: 100%;
  margin-bottom: 10px;

  &:disabled {
    background-color: grey;
    cursor: not-allowed;
  }
`;

const BackButton = styled.button`
  padding: 10px;
  background-color: gray;
  color: white;
  border: none;
  cursor: pointer;
  width: 150px;
  position: absolute;
  bottom: 20px;
  right: 20px;
`;

const RecipientList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 5px;
  max-height: 100px;
  overflow-y: auto;
  margin-bottom: 20px;
`;

const RecipientItem = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 10px;
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 5px;
`;

const RemoveButton = styled.button`
  background: none;
  border: none;
  color: red;
  cursor: pointer;
  font-size: 16px;

  &:hover {
    font-weight: bold;
  }
`;

// 새로 추가된 스타일
const ImageUploadContainer = styled.div`
  margin-top: 10px;
`;

const ImagePreview = styled.img`
  max-width: 100%;
  max-height: 200px;
  margin-top: 10px;
  border: 1px solid #ddd;
`;

const FileInput = styled.input`
  margin-top: 10px;
`;

const MessageSendPage = ({ setActivePage, previousMessage }) => {
  const [message] = useState(previousMessage);
  const [phoneNumber, setPhoneNumber] = useState({
    part1: '',
    part2: '',
    part3: '',
  });
  const [recipients, setRecipients] = useState([]);
  const [isSending, setIsSending] = useState(false);
  const [selectedImage, setSelectedImage] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [base64Image, setBase64Image] = useState(null);

  const byteCount = new TextEncoder().encode(message).length;
  const isMMS = byteCount > 90;

  const handlePhoneNumberChange = (e, part) => {
    setPhoneNumber({
      ...phoneNumber,
      [part]: e.target.value,
    });
  };

  const handleAddPhoneNumber = () => {
    const fullNumber = `${phoneNumber.part1}${phoneNumber.part2}${phoneNumber.part3}`;
    if (!recipients.includes(fullNumber) && fullNumber.length === 11) {
      setRecipients([...recipients, fullNumber]);
      setPhoneNumber({ part1: '', part2: '', part3: '' });
    }
  };

  const handleRemoveRecipient = (number) => {
    setRecipients(recipients.filter((recipient) => recipient !== number));
  };

  const handleImageChange = async (e) => {
    const file = e.target.files[0];
    if (file) {
      try {
        // 파일 형식 확인
        if (!['image/png'].includes(file.type)) {
          alert('PNG 형식의 이미지만 업로드 가능합니다.');
          return;
        }
  
        setSelectedImage(file);
        setImagePreview(URL.createObjectURL(file));
  
        // 서버에 업로드하여 URL 생성
        const userId = sessionStorage.getItem("userId") || "123";
        const uploadedUrl = await uploadImageToS3(file, userId);
        console.log('Uploaded URL:', uploadedUrl);
        if (uploadedUrl) {
          setBase64Image(uploadedUrl); // URL을 상태에 저장
        } else {
          throw new Error('URL 생성 실패');
        }
      } catch (error) {
        alert('이미지 업로드 실패: ' + error.message);
      }
    } else {
      setSelectedImage(null);
      setImagePreview(null);
      setBase64Image(null);
    }
  };  

  const handleSendMessage = async () => {
    if (isMMS && selectedImage) {
      const fileUrl = base64Image;
  
      setIsSending(true);
      try {
        await Promise.all(
          recipients.map((recipient) =>
            sendMMS(recipient, message, fileUrl, {})
          )
        );
        setActivePage('SendSuccessPage');
      } catch (error) {
        alert('일부 메시지 전송 실패: ' + error.message);
      } finally {
        setIsSending(false);
      }
    } else {
      // SMS 전송 처리
      setIsSending(true);
      try {
        await Promise.all(
          recipients.map((recipient) => sendSMS(recipient, message))
        );
        setActivePage('SendSuccessPage');;
      } catch (error) {
        alert('일부 메시지 전송 실패: ' + error.message);
      } finally {
        setIsSending(false);
      }
    }
  };
  

  return (
    <PageContainer>
      <FormContainer>
        <LeftPane>
          <SectionTitle>메시지 전송</SectionTitle>
          <Textarea value={message} readOnly />
          {isMMS && (
            <>
              <MessageTypeInfo>메시지가 90bytes를 넘어가므로 MMS로 자동 전환됩니다.</MessageTypeInfo>
              <SectionTitle>이미지</SectionTitle>
              <ImageUploadContainer>
                <FileInput type="file" accept="image/png" onChange={handleImageChange} />
                {imagePreview && <ImagePreview src={imagePreview} alt="이미지 미리보기" />}
              </ImageUploadContainer>
              <ImageTypeInfo>이미지의 형식은 PNG만 가능합니다.</ImageTypeInfo>
              <ImageTypeInfo>이미지는 최대 1개까지 전송 가능합니다.</ImageTypeInfo>
            </>
          )}
        </LeftPane>
        <RightPane>
          <NumberInputSection>
            <SubSectionTitle>번호 입력</SubSectionTitle>
            <NumberInputGroup>
              <input
                type="text"
                maxLength="3"
                value={phoneNumber.part1}
                onChange={(e) => handlePhoneNumberChange(e, 'part1')}
              />
              <input
                type="text"
                maxLength="4"
                value={phoneNumber.part2}
                onChange={(e) => handlePhoneNumberChange(e, 'part2')}
              />
              <input
                type="text"
                maxLength="4"
                value={phoneNumber.part3}
                onChange={(e) => handlePhoneNumberChange(e, 'part3')}
              />
            </NumberInputGroup>
            <SubmitButton onClick={handleAddPhoneNumber}>번호 추가</SubmitButton>
          </NumberInputSection>
          <NumberInputSection>
            <SubSectionTitle>받는 사람</SubSectionTitle>
            <RecipientList>
              {recipients.map((recipient, index) => (
                <RecipientItem key={index}>
                  {recipient}
                  <RemoveButton onClick={() => handleRemoveRecipient(recipient)}>X</RemoveButton>
                </RecipientItem>
              ))}
            </RecipientList>
            <SubmitButton
              onClick={handleSendMessage}
              disabled={recipients.length === 0 || isSending}
            >
              {isSending ? '전송 중...' : '발송하기'}
            </SubmitButton>
          </NumberInputSection>
        </RightPane>
      </FormContainer>
      <BackButton onClick={() => setActivePage('MessageInput')}>뒤로가기</BackButton>
    </PageContainer>
  );
};

export default MessageSendPage;
