class ScoreService {
	static getScore(playerName) {
		return fetch(`http://localhost:8080/leaderboard/getScore?player=${playerName}`)
			.then(response => {
				if (!response.ok) {
					throw new Error(`Failed to fetch score for ${playerName}`);
				}
				return response.json();
			})
			.then(data => {
				console.log(data);  // Log the response data for debugging
				return data;
			})
			.catch(error => {
				console.error(error);
				return 'Score not available';
			});
	}
}


var stompClient = null;

function connect() {
	var socket = new SockJS('http://localhost:8080/websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function (frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/leaderboardUpdates', function (message) {
			var contentType = message.headers['content-type'];
			var messageContent = message.body;	

			if (contentType === 'application/json') {
				updateLeaderboard(JSON.parse(messageContent));
			} else if (contentType === 'text/plain;charset=UTF-8') {
				updateLeaderboardText(messageContent);
			} else {
				console.error('Unsupported content type: ' + contentType);
			}
		});
	});
}

function updateLeaderboard(playerNames) {
	
	var leaderboardDiv = document.getElementById('leaderboard');

	// Clear the leaderboard content
	leaderboardDiv.innerHTML = '';

	// Create a table element
	var table = document.createElement('table');
	table.classList.add('leaderboard-table'); // Add a class for custom styling

	// Create table header
	var headerRow = table.createTHead().insertRow(0);
	var headerCell = headerRow.insertCell(0);
	headerCell.textContent = 'Player Names';
	var headerCellScore = headerRow.insertCell(1);
	headerCellScore.textContent = 'Get Score';

	// Create table rows for player names and buttons
	for (var i = 0; i < playerNames.length; i++) {
		var row = table.insertRow(i + 1);

		// Player Name Cell
		var cellName = row.insertCell(0);
		cellName.textContent = playerNames[i];

		// Button Cell
		var cellButton = row.insertCell(1);
		var button = document.createElement('button');
		button.textContent = 'Get Score';
		button.classList.add('get-score-button'); // Add a class for custom styling
		button.onclick = function () {
			// Handle button click to get and display the score
			var playerName = this.parentNode.parentNode.cells[0].textContent;

			ScoreService.getScore(playerName)
				.then(score => {
					// Update the score in the table cell
					var scoreCell = this.parentNode.parentNode.cells[1];
					scoreCell.textContent = score;
				})
				.catch(error => {
					console.error(error);
					// Optionally update the cell to indicate an error
					var scoreCell = this.parentNode.parentNode.cells[1];
					scoreCell.textContent = 'Error';
				});
		};
		cellButton.appendChild(button);
	}

	// Append the table to the leaderboardDiv
	leaderboardDiv.appendChild(table);
}


function updateLeaderboardText(message) {
	// Update the leaderboard content on the page for plain text messages
	var leaderboardDiv = document.getElementById('leaderboard');
	leaderboardDiv.innerHTML = '<p>Leaderboard Update: ' + message + '</p>';
}

connect();