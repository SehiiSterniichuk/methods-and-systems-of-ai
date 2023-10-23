import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import TravellingSalesmanApp from './travelling-salesman-web/TravellingSalesmanApp';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Routes} from "react-router-dom";
import ExpertSystemApp from "./expert-system-web/ExpertSystemApp";

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <BrowserRouter>
      <Routes>
          <Route path="" element={<TravellingSalesmanApp />}>
          </Route>
          <Route path="/travelling-salesman-problem" element={<TravellingSalesmanApp />}>
          </Route>
          <Route path="/expert-system" element={<ExpertSystemApp />}>
          </Route>
      </Routes>
  </BrowserRouter>
);



// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
