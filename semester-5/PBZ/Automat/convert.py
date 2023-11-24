import pandas as pd
from neo4j import GraphDatabase
from os import environ

AURA_CONNECTION_URI = "neo4j+s://8161d654.databases.neo4j.io"
AURA_USERNAME = "neo4j"
AURA_PASSWORD = environ["NEO4J_PASSWORD"]

driver = GraphDatabase.driver(
    AURA_CONNECTION_URI,
    auth=(AURA_USERNAME, AURA_PASSWORD)
)


def upload_teams():
    teams_csv = pd.read_csv("teams.csv", index_col=0)
    
    with driver.session() as session:
        for index, row in teams_csv.iterrows():
            name = row['name']
            query = "MERGE (t:Team {team_id: " + str(index) + "}) ON CREATE SET t.name = '" + name + "'"
            session.run(query)

def upload_leagues():
    leagues_csv = pd.read_csv("leagues.csv", index_col=0)

    with driver.session() as session:
        for index, row in leagues_csv.iterrows():
            name = row['name']
            understat_notation = row['understatNotation']

            query = "MERGE (t:League {league_id: " + str(index) + "}) ON CREATE SET t.name = '" + name + "', t.understat_notation = '" + understat_notation + "'"
            session.run(query)

def upload_players():
    players_csv = pd.read_csv("players.csv", index_col=0)

    with driver.session() as session:
        for index, row in players_csv.iterrows():
            name = row["name"]

            query = "MERGE (t:Player {player_id: " + str(index) + "}) ON CREATE SET t.name = '" + name + "'"
            session.run(query)

def upload_games():
    games_csv = pd.read_csv("games.csv", index_col=0)

    with driver.session() as session:
        for index, row in games_csv[4000:].iterrows():
            league_id = row["leagueID"]
            season = row["season"]
            date = row["date"]
            home_team_id = row["homeTeamID"]
            away_team_id = row["awayTeamID"]
            home_goals = row["homeGoals"]
            away_goals = row["awayGoals"]

            session.run("""
                        MERGE (g:Game {game_id: $game_id}) ON CREATE SET
                            g.season = $season,
                            g.date = $date,
                            g.home_goals = $home_goals,
                            g.away_goals = $away_goals
                        
                        MERGE (ht: Team {team_id: $home_team})   
                        MERGE (ht)<-[:HOME_TEAM]-(g)

                        MERGE (at: Team {team_id: $away_team})
                        MERGE (at)<-[:AWAY_TEAM]-(g)

                        MERGE (g)-[:LEAGUE]->(l: League {league_id: $league_id})
                        """,
                        game_id=index,
                        home_team=home_team_id,
                        away_team=away_team_id,
                        league_id=league_id,
                        home_goals=home_goals,
                        away_goals=away_goals,
                        season=season,
                        date=date)


def upload_appearances():
    appearances_csv = pd.read_csv("appearances.csv", index_col=-1)

    with driver.session() as session:
        for index, row in appearances_csv[:2000].iterrows():
            game_id = row["gameID"]
            player_id = row["playerID"]
            goals = row["goals"]
            position = row["position"]

            session.run("""
                        MERGE (g: Game { game_id: $game_id })
                              <-[:PLAYED_IN { goals: $goals, position: $position }]
                              -(p: Player { player_id: $player_id })
                        """,
                        game_id=game_id,
                        goals=goals,
                        position=position,
                        player_id=player_id)

upload_teams()
upload_leagues()
upload_players()
upload_games()
upload_appearances()
