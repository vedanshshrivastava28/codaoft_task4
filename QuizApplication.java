import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class QuizApplication extends JFrame {

    private List<QuizQuestion> questions;
    private int currentQuestionIndex;
    private int score;
    private Timer timer;
    private JLabel questionLabel;
    private ButtonGroup optionGroup;
    private JRadioButton[] optionButtons;
    private JButton submitButton;

    public QuizApplication() {
        questions = new ArrayList<>();
        currentQuestionIndex = 0;
        score = 0;
        timer = new Timer();

        setTitle("Quiz Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 400));
        setLayout(new BorderLayout());

        questionLabel = new JLabel("", JLabel.CENTER);
        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        optionGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }
        add(optionsPanel, BorderLayout.CENTER);

        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedOptionIndex = getSelectedOptionIndex();
                if (selectedOptionIndex != -1) {
                    submitAnswer(selectedOptionIndex);
                } else {
                    JOptionPane.showMessageDialog(QuizApplication.this,
                            "Please select an option.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        add(submitButton, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen

        // Sample questions
        QuizQuestion question1 = new QuizQuestion("What is the capital of France?",
                new String[]{"London", "Berlin", "Paris", "Madrid"}, 2);
        QuizQuestion question2 = new QuizQuestion("Which planet is known as the Red Planet?",
                new String[]{"Mars", "Venus", "Earth", "Jupiter"}, 0);

        questions.add(question1);
        questions.add(question2);

        startQuiz();
    }

    private void startQuiz() {
        displayQuestion(currentQuestionIndex);
    }

    private void displayQuestion(int index) {
        if (index < questions.size()) {
            QuizQuestion quizQuestion = questions.get(index);
            questionLabel.setText(quizQuestion.getQuestion());
            String[] options = quizQuestion.getOptions();
            for (int i = 0; i < options.length; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setSelected(false);
            }
            startTimer();
        } else {
            finishQuiz();
        }
    }

    private void startTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                displayTimeoutMessage();
                moveToNextQuestion();
            }
        }, 15000); // 15 seconds timer
    }

    private void submitAnswer(int selectedOptionIndex) {
        QuizQuestion currentQuestion = questions.get(currentQuestionIndex);
        if (selectedOptionIndex == currentQuestion.getCorrectOptionIndex()) {
            score++;
        }
        moveToNextQuestion();
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        displayQuestion(currentQuestionIndex);
    }

    private void finishQuiz() {
        timer.cancel();
        displayResult();
    }

    private void displayResult() {
        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Quiz completed!\n\n");
        resultMessage.append("Your Score: ").append(score).append(" / ").append(questions.size()).append("\n\n");

        for (int i = 0; i < questions.size(); i++) {
            QuizQuestion quizQuestion = questions.get(i);
            resultMessage.append("Question ").append(i + 1).append(": ").append(quizQuestion.getQuestion()).append("\n");
            resultMessage.append("Correct Answer: ").append(quizQuestion.getCorrectAnswer()).append("\n\n");
        }

        JOptionPane.showMessageDialog(this,
                resultMessage.toString(),
                "Quiz Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // Close the application after displaying result
    }

    private void displayTimeoutMessage() {
        JOptionPane.showMessageDialog(this,
                "Time's up! Moving to the next question.", "Timeout", JOptionPane.INFORMATION_MESSAGE);
    }

    private int getSelectedOptionIndex() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                QuizApplication quizApp = new QuizApplication();
                quizApp.setVisible(true);
            }
        });
    }
}

class QuizQuestion {
    private String question;
    private String[] options;
    private int correctOptionIndex;

    public QuizQuestion(String question, String[] options, int correctOptionIndex) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public String getCorrectAnswer() {
        return options[correctOptionIndex];
    }
}
