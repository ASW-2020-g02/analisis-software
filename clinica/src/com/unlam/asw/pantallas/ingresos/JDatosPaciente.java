package com.unlam.asw.pantallas.ingresos;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.unlam.asw.DB.DAO;
import com.unlam.asw.entities.Paciente;
import com.unlam.asw.pantallas.general.JIngresos;

public class JDatosPaciente extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DAO dao;
	private JFrame frame;
	private JTextField txtCodPaciente;
	private JTextField txtNombrePaciente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JDatosPaciente frame = new JDatosPaciente();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JDatosPaciente() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UIManager.put("OptionPane.yesButtonText", "Si");
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "�Est� seguro que desea salir?", "Atenci�n",
						JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION) {
					DAO.obtenerInstancia().cerrar();
					setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				} else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});
		setBounds(100, 100, 429, 404);
		setTitle("Alta de paciente");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblCodPaciente = new JLabel("C\u00F3digo");
		lblCodPaciente.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblCodPaciente.setBounds(72, 95, 80, 30);
		panel.add(lblCodPaciente);

		txtCodPaciente = new JTextField();
		txtCodPaciente.setBounds(162, 176, 189, 20);
		panel.add(txtCodPaciente);
		txtCodPaciente.setColumns(10);

		JLabel lblAltaDePaciente = new JLabel("Alta de Paciente");
		lblAltaDePaciente.setHorizontalAlignment(SwingConstants.CENTER);
		lblAltaDePaciente.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblAltaDePaciente.setBounds(41, 14, 331, 25);
		panel.add(lblAltaDePaciente);

		txtNombrePaciente = new JTextField();
		txtNombrePaciente.setBounds(162, 103, 189, 20);
		panel.add(txtNombrePaciente);
		txtNombrePaciente.setColumns(10);

		JLabel lblNombreDelPaciente = new JLabel("Nombre");
		lblNombreDelPaciente.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNombreDelPaciente.setBounds(72, 168, 80, 30);
		panel.add(lblNombreDelPaciente);

		JButton btnConfirmar = new JButton("<html><center>Confirmar</center></html>");
		btnConfirmar.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnConfirmar.setFocusPainted(false);
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generarAltaPaciente();
			}
		});
		btnConfirmar.setBounds(36, 276, 156, 48);
		panel.add(btnConfirmar);

		JButton btnSalir = new JButton("<html><center>Cancelar</center></html>");
		btnSalir.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnSalir.setFocusPainted(false);
		btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JIngresos jp = new JIngresos();
				jp.setVisible(true);
				dispose();
			}
		});
		btnSalir.setBounds(223, 276, 156, 48);
		panel.add(btnSalir);

		dao = DAO.obtenerInstancia();
		setLocationRelativeTo(null);
	}

	private void generarAltaPaciente() {
		String strCod = txtCodPaciente.getText().trim();
		String strNombre = txtNombrePaciente.getText().trim();
		int nombreLength = strNombre.length();

		// Validacion de parse-int
		if (esCodigoValido(strCod)) {
			int cod = Integer.parseInt(strCod);

			// Busca el paciente en una query, si existe devuelve true
			if (!existePaciente(cod)) {
				// Si el paciente no existe, chequeamos que se haya ingresado bien el nombre
				if (nombreLength <= 50 && nombreLength > 0) {
					try {
						registrarPaciente(new Paciente(strCod, strNombre));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Paciente registrado con �xito en la base de datos.",
							"Paciente registrado", JOptionPane.INFORMATION_MESSAGE);

					// Reseteo los input
					txtCodPaciente.setText("");
					txtNombrePaciente.setText("");
				} else {
					JOptionPane.showMessageDialog(null,
							"El nombre ingresado excede el l�mite de 50 caracteres, o est� vac�o.",
							"Paciente registrado", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "�El paciente ya existe!", "Error",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "�El c�digo ingresado no es v�lido!", "Error",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public boolean esCodigoValido(String codigo) {
		try {
			Integer.parseInt(codigo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean existePaciente(int cod) {
		Paciente paciente = null;
		try {
			paciente = dao.buscarPacientePorCodigo(cod);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}

		if (paciente == null) {
			return false;
		} else {
			return true;
		}
	}

	public void registrarPaciente(Paciente pac) {
		try {
			dao.insertarPaciente(pac);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
