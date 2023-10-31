import React, {useState} from 'react';
import "./styles/default-expert-system.scss"
import ExpertPage from "./components/expert/ExpertPage";
import UserPage from "./components/user/UserPage";

enum Mode {
    DEFAULT, EXPERT, USER
}

function ExpertSystemApp() {
    const [mode, setMode] = useState(Mode.DEFAULT);
    const defaultPage = <>
        <div className={"expert-system-mode-wrapper"}>
            <h1>Expert System</h1>
            <div className="button-wrapper">
                <button onClick={() => setMode(Mode.EXPERT)}>Expert mode</button>
                <button onClick={() => setMode(Mode.USER)}>User mode</button>
            </div>
        </div>
    </>;
    const expertPage = <ExpertPage/>
    const userPage = <UserPage/>;
    let page;
    switch (mode) {
        case Mode.DEFAULT:
            page = defaultPage;
            break;
        case Mode.USER:
            page = userPage;
            break;
        case Mode.EXPERT:
            page = expertPage;
            break;
    }
    return page;
}

export default ExpertSystemApp;