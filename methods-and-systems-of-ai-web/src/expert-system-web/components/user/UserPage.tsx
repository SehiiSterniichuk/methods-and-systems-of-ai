import React, {useState} from 'react';
import SearchPage from "./SearchPage";
import ChatPage from "./ChatPage";

function UserPage() {
    const [startChatRuleId, setStartChatRuleId] = useState(-1);
    const page = startChatRuleId >= 0 ? <ChatPage startChatId={startChatRuleId}/> :
        <SearchPage setRuleId={setStartChatRuleId}/>;
    return (
        <main className={"user-page-main"}>
            {page}
        </main>
    );
}

export default UserPage;