node ('master') {
	checkout scm
	stage('Build') {
		withMaven(maven: 'M3') {
		
			if (isUnix()) {
				sh 'mvn -Dmaven.test.failure.ignore clean package'
			}
			else {
				bat 'mvn -Dmaven.test.failure.ignore clean package'
			}
		}
	}
	stage('Results') {
		junit '**/target/surefire-reports/Test-*.xml'
		archive 'target/*.jar'
	}
}
