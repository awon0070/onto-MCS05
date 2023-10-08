const http = require("http");
const fs = require('fs').promises;
const path = require('path');
const host = '0.0.0.0';
const localhost = '192.168.1.101';
const url = require('url'); // Import the 'url' module for parsing URLs
const port = process.env.PORT || 8000;

const requestListener = function (req, res) {
  const requestUrl = req.url; // Rename the variable to 'requestUrl'

  // Define a mapping of routes to HTML files
  const routes = {
    '/': '/real_main.html', // Default route
    '/aboutUs.html': '/aboutUs.html',
    '/heart_disease_info.html': '/heart_disease_info.html',
    '/heart.jpeg': '/heart.jpeg',
    '/style.css': '/style.css',
  };

  const htmlFile = routes[requestUrl];

  if (htmlFile) {
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
};

const server = http.createServer(requestListener);
server.listen(port, host, () => {
  console.log(`Server is running on http://${localhost}:${port}`);
});










