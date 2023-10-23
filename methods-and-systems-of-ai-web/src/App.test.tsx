import React from 'react';
import { render, screen } from '@testing-library/react';
import TravellingSalesmanApp from './travelling-salesman-web/TravellingSalesmanApp';

test('renders learn react link', () => {
  render(<TravellingSalesmanApp />);
  const linkElement = screen.getByText(/learn react/i);
  expect(linkElement).toBeInTheDocument();
});
