const http = require("http");
const fs = require('fs').promises;
const path = require('path');
const host = '0.0.0.0';
const localhost = 'localhost';
const url = require('url'); // Import the 'url' module for parsing URLs
const port = process.env.PORT || 8000;

//const requestListener = function (req, res) {
//   const requestUrl = req.url; // Rename the variable to 'requestUrl'

//   // Define a mapping of routes to HTML files
//   const routes = {
//     '/': '/real_main.html',
//     '/real_main.html': '/real_main.html', // Default route
//     '/aboutUs.html': '/aboutUs.html',
//     '/heart_disease_info.html': '/heart_disease_info.html',
//     '/heart.jpeg': '/heart.jpeg',
//     '/style.css': '/style.css',
//     '/api/getData': handleGetDataRequest, // New API endpoint
//   };

//   const htmlFile = routes[requestUrl];

//   if (htmlFile) {
//     const filePath = path.join(__dirname, htmlFile);

//     fs.readFile(filePath)
//       .then(contents => {
//         const contentType =
//           htmlFile.endsWith('.html') ? 'text/html' : 'image/jpeg';
//         res.setHeader("Content-Type", contentType);
//         res.writeHead(200);
//         res.end(contents);
//       })
//       .catch(err => {
//         res.writeHead(500);
//         res.end(err);
//       });
//   } else {
//     // If the request URL doesn't match any known route, return a 404 response.
//     res.writeHead(404);
//     res.end('404 Not Found');
//   }
// };

const requestListener = function (req, res) {
  const requestUrl = req.url;

  const routes = {
    '/': '/real_main.html',
    '/real_main.html': '/real_main.html',
    '/aboutUs.html': '/aboutUs.html',
    '/heart_disease_info.html': '/heart_disease_info.html',
    '/heart.jpeg': '/heart.jpeg',
    '/style.css': '/style.css',
    '/api/getData': handleGetDataRequest,
  };

  const htmlFile = routes[requestUrl];

  if (htmlFile) {
    if (requestUrl.endsWith('.css')) {
      // If the requested URL ends with .css, set the content type to text/css
      const filePath = path.join(__dirname, htmlFile);

      fs.readFile(filePath)
        .then(contents => {
          res.setHeader("Content-Type", 'text/css');
          res.writeHead(200);
          res.end(contents);
        })
        .catch(err => {
          res.writeHead(500);
          res.end(err);
        });
    } else {
      const filePath = path.join(__dirname, htmlFile);
      fs.readFile(filePath)
        .then(contents => {
          const contentType =
            htmlFile.endsWith('.html') ? 'text/html' : 'image/jpeg';
          res.setHeader("Content-Type", contentType);
          res.writeHead(200);
          res.end(contents);
        })
        .catch(err => {
          res.writeHead(500);
          res.end(err);
        });
    }
  } else {
    res.writeHead(404);
    res.end('404 Not Found');
  }
};


// Handle the '/api/getData' endpoint
function handleGetDataRequest(req, res) {
  // Simulate some data as an example
  const data = {
    name: 'Example Heart Disease',
    IRI: '12345',
    symptoms: 'Example Symptom 1, Example Symptom 2',
  };

  // Convert the data to a JSON string
  const jsonData = JSON.stringify(data);

  // Set the Content-Type header to JSON
  res.setHeader('Content-Type', 'application/json');
  res.writeHead(200);
  res.end(jsonData);
}

const server = http.createServer(requestListener);
server.listen(port, host, () => {
  console.log(`Server is running on http://${localhost}:${port}`);
});

// Add an event listener to the search button





